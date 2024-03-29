package com.mes.hostcheckout.sample.ui;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mes.hostcheckout.sample.model.Customer;
import com.mes.hostcheckout.sample.repo.CustomerRepository;
import com.mes.hostcheckout.sample.ui.CustomerEditor;

import javax.annotation.PostConstruct;

@RunWith(MockitoJUnitRunner.class)
public class CustomerEditorTests {

	private static final String FIRST_NAME = "John";
	private static final String LAST_NAME = "Doe";
	private static final String AMOUNT = "0";

	@Mock CustomerRepository customerRepository;
	@InjectMocks CustomerEditor editor;
	@Mock CustomerEditor.ChangeHandler changeHandler;

	@Before
	public void init() {
		editor.setChangeHandler(changeHandler);
	}

	@Test
	public void shouldStoreCustomerInRepoWhenEditorSaveClicked() {
		emptyCustomerWasSetToForm();

		this.editor.firstName.setValue(FIRST_NAME);
		this.editor.lastName.setValue(LAST_NAME);
		this.editor.amount.setValue(AMOUNT);

		this.editor.save();

		then(this.customerRepository).should().save(argThat(customerMatchesEditorFields()));
	}

	@Test
	public void shouldDeleteCustomerFromRepoWhenEditorDeleteClicked() {
		customerDataWasFilled();

		editor.delete();

		then(this.customerRepository).should().delete(argThat(customerMatchesEditorFields()));
	}

	private void emptyCustomerWasSetToForm() {
		this.editor.editCustomer(new Customer());
	}
	private void customerDataWasFilled() {
		this.editor.editCustomer(new Customer(FIRST_NAME, LAST_NAME, AMOUNT));
	}

	private ArgumentMatcher<Customer> customerMatchesEditorFields() {
		return customer -> FIRST_NAME.equals(customer.getFirstName()) && LAST_NAME.equals(customer.getLastName()) && AMOUNT.equals(customer.getAmount());
	}

}
