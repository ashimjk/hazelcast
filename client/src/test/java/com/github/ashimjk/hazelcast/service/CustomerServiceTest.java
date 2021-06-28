package com.github.ashimjk.hazelcast.service;

import com.github.ashimjk.hazelcast.ClientApplication;
import com.github.ashimjk.hazelcast.config.ClientTestConfiguration;
import com.github.ashimjk.hazelcast.domain.Customer;
import com.github.ashimjk.hazelcast.model.Address;
import com.github.ashimjk.hazelcast.model.AddressKey;
import com.github.ashimjk.hazelcast.model.CustomerOverview;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {ClientApplication.class, ClientTestConfiguration.class})
class CustomerServiceTest {

    @Autowired private CustomerService customerService;
    @Autowired private HazelcastInstance clientInstance;
    @Autowired private StorageNodeFactory storageNodeFactory;

    @BeforeEach
    public void tearDown() {
        clientInstance.getMap(CustomerService.CUSTOMERS_MAP).clear();
    }

    @Test
    public void testAddCustomer() {
        Customer customer = new Customer(1L, "Ashim Khadka", LocalDate.now(), "ashim@gmail.com");
        customerService.addCustomer(customer);

        IMap<Long, Customer> customersMap = clientInstance.getMap(CustomerService.CUSTOMERS_MAP);
        assertEquals(1, customersMap.size());
        assertEquals(customer, customersMap.get(1L));
    }

    @Test
    public void testAddCustomers() {
        Customer customer1 = new Customer(1L, "Ashim Khadka", LocalDate.now(), "ashim@gmail.com");
        Customer customer2 = new Customer(2L, "Kushal Sherchan", LocalDate.now(), "kushal@gmail.com");
        Customer customer3 = new Customer(3L, "Shekhar Rai", LocalDate.now(), "shekar@gmail.com");

        List<Customer> customers = Arrays.asList(customer1, customer2, customer3);

        customerService.addCustomers(customers);

        IMap<Long, Customer> customersMap = clientInstance.getMap(CustomerService.CUSTOMERS_MAP);
        assertEquals(3, customersMap.size());
        assertEquals(customer1, customersMap.get(1L));
        assertEquals(customer2, customersMap.get(2L));
        assertEquals(customer3, customersMap.get(3L));
    }

    @Test
    public void testUpdateCustomer() {
        customerService.addCustomers(generateCustomers(2));

        assertEquals("Customer 1", customerService.getCustomer(1L).getName());

        boolean result
                = customerService.updateCustomer(1L,
                                                 customer -> {
                                                     customer.setName("Ashim Khadka");
                                                     return customer;
                                                 });

        assertTrue(result);
        assertEquals("Ashim Khadka", customerService.getCustomer(1L).getName());
    }

    @Test
    public void testUpdateCustomerWithEntryProcessor() {
        customerService.addCustomers(generateCustomers(2));

        assertEquals("Customer 1", customerService.getCustomer(1L).getName());

        LocalDate newDOB = LocalDate.now().plusYears(2);
        UpdateCustomerDOBEP entryProcessor = new UpdateCustomerDOBEP(newDOB);
        boolean result = customerService.updateCustomerWithEntryProcessor(1L, entryProcessor);

        assertTrue(result);
        assertEquals(newDOB, customerService.getCustomer(1L).getDob());
    }

    @Test
    public void testNoDataLossWithOnlyOneNode() throws Exception {
        storageNodeFactory.ensureClusterSize(4);

        int maxCustomers = 1000;
        List<Customer> customers = generateCustomers(maxCustomers);
        customerService.addCustomers(customers);

        IMap<Long, Customer> customersMap = clientInstance.getMap(CustomerService.CUSTOMERS_MAP);
        assertEquals(maxCustomers, customersMap.size());
        storageNodeFactory.ensureClusterSize(1);
        assertEquals(maxCustomers, customersMap.size());
    }

    @Test
    public void testNoDataLossAfterClusterShutdown() throws Exception {
        storageNodeFactory.ensureClusterSize(4);

        int maxCustomers = 1000;
        List<Customer> customers = generateCustomers(maxCustomers);

        customerService.addCustomers(customers);

        IMap<Long, Customer> customersMap = clientInstance.getMap(CustomerService.CUSTOMERS_MAP);
        assertEquals(maxCustomers, customersMap.size());

        storageNodeFactory.ensureClusterSize(0); // Shutdown all storage nodes
        storageNodeFactory.ensureClusterSize(3); // Start another 3 storage nodes

        assertEquals(maxCustomers, customersMap.size());
    }

    @Test
    public void testSearchForCustomersWithDob() {
        customerService.addCustomers(generateCustomers(10));

        Collection<Customer> customers = customerService.findCustomer(LocalDate.now(), LocalDate.now().plusYears(1));
        assertEquals(1, customers.size());
    }

    @Test
    public void testFindCustomersByEmail() {
        customerService.addCustomers(generateCustomers(10));

        Collection<Customer> customers = customerService.findCustomersByEmail("%@gmail.com");
        assertEquals(10, customers.size());
    }

    @Test
    public void testGetCustomerOverview() {
        Long customerId = 1L;
        Long addressId = 5L;
        Customer customer = new Customer(customerId, "Ashim Khadka", LocalDate.now(), "ashim@gmail.com");
        Address address = new Address(addressId, customerId, "KTM", "Nepal");
        AddressKey addressKey = new AddressKey(addressId, customerId);

        clientInstance.getMap(MapNames.ADDRESSES_MAP).put(addressKey, address);
        clientInstance.getMap(MapNames.CUSTOMERS_MAP).put(customerId, customer);

        CustomerOverview customerOverview = customerService.getCustomerOverview(customerId);
        assertNotNull(customerOverview);
        assertEquals("Ashim Khadka", customerOverview.getCustomerName());
        assertEquals(1, customerOverview.getAddresses().size());
        assertEquals("KTM", customerOverview.getAddresses().get(0).getCity());
    }

    private List<Customer> generateCustomers(int maxCustomers) {
        List<Customer> customers = new ArrayList<>(maxCustomers);

        for (long x = 0; x < maxCustomers; x++) {
            customers.add(new Customer(
                    x,
                    "Customer " + x,
                    LocalDate.now().plusYears(x),
                    "customer" + x + "@gmail.com")
            );
        }

        return customers;
    }

}