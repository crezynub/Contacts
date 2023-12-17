package Skillbox;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
@Component
public class ContactFieldSetMapper implements FieldSetMapper <Contact>{
@Override
    public Contact mapFieldSet(FieldSet fieldSet) throws BindException {
        Contact contact = new Contact();
        contact.setFullName(fieldSet.readString(0));
        contact.setPhoneNumber(fieldSet.readString(1));
        contact.setEmail(fieldSet.readString(2));
        return contact;
    }
}
