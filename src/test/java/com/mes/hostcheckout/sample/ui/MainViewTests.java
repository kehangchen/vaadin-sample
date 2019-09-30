package com.mes.hostcheckout.sample.ui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.mes.hostcheckout.sample.model.Customer;
import com.mes.hostcheckout.sample.repo.CustomerRepository;
import com.mes.hostcheckout.sample.ui.CustomerEditor;
import com.mes.hostcheckout.sample.ui.MainView;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.VaadinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainViewTests.Config.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MainViewTests {

	@Autowired CustomerRepository repository;

	VaadinRequest vaadinRequest = Mockito.mock(VaadinRequest.class);

	CustomerEditor editor;

	MainView mainView;

	@Before
	public void setup() {
		this.editor = new CustomerEditor(this.repository);
		this.mainView = new MainView(this.repository, editor);
	}

	@Test
	public void shouldInitializeTheGridWithCustomerRepositoryData() {
		int customerCount = (int) this.repository.count();

		then(mainView.grid.getColumns()).hasSize(3);
		then(getCustomersInGrid()).hasSize(customerCount);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Customer> getCustomersInGrid() {
		ListDataProvider<Customer> ldp = (ListDataProvider) mainView.grid.getDataProvider();
		return new ArrayList<>(ldp.getItems());
	}

	@Test
	public void shouldFillOutTheGridWithNewData() {
		int initialCustomerCount = (int) this.repository.count();

		customerDataWasFilled(editor, "John", "Doe", "0");

		this.editor.save();

		then(getCustomersInGrid()).hasSize(initialCustomerCount + 1);

		then(getCustomersInGrid().get(getCustomersInGrid().size() - 1))
			.extracting("firstName", "lastName")
			.containsExactly("John", "Doe");

	}

	@Test
	public void shouldFilterOutTheGridWithTheProvidedLastName() {

		this.repository.save(new Customer("Merry", "May", "0"));

		mainView.listCustomers("Long");

		then(getCustomersInGrid()).hasSize(1);
		then(getCustomersInGrid().get(getCustomersInGrid().size() - 1))
			.extracting("firstName", "lastName", "amount")
			.containsExactly("Merry", "May", "0");
	}

	@Test
	public void shouldInitializeWithInvisibleEditor() {

		then(this.editor.isVisible()).isFalse();
	}

	@Test
	public void shouldMakeEditorVisible() {
		Customer first = getCustomersInGrid().get(0);
		this.mainView.grid.select(first);

		then(this.editor.isVisible()).isTrue();
	}

	private void customerDataWasFilled(CustomerEditor editor, String firstName,
			String lastName, String amount) {
		this.editor.firstName.setValue(firstName);
		this.editor.lastName.setValue(lastName);
		editor.editCustomer(new Customer(firstName, lastName, amount));
	}

	@Configuration
	@EnableAutoConfiguration(exclude = com.vaadin.flow.spring.SpringBootAutoConfiguration.class)
	static class Config {

		@Autowired
		CustomerRepository repository;

		@PostConstruct
		public void initializeData() {
			this.repository.save(new Customer("Jack", "Bauer", "10"));
			this.repository.save(new Customer("Chloe", "O'Brian", "20"));
			this.repository.save(new Customer("Kim", "Bauer", "30"));
			this.repository.save(new Customer("David", "Palmer", "80"));
			this.repository.save(new Customer("Michelle", "Dessler", "90"));
		}
	}
}
