package Skillbox;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ContactsWriter {
    private FlatFileItemWriter<Contact> itemWriter = new FlatFileItemWriter<>();
    private BeanWrapperFieldExtractor fieldExtractor = new BeanWrapperFieldExtractor<>();
    private DelimitedLineAggregator<Contact> lineAggregator = new DelimitedLineAggregator<>();
    @Value("${app.data.source}")
    private String SOURCE_DATA;

    @PostConstruct
    private void init() {
        fieldExtractor.setNames(new String[]{"fullName", "phoneNumber", "email"});
        fieldExtractor.afterPropertiesSet();

        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);
        itemWriter.setLineAggregator(lineAggregator);
        itemWriter.setResource(new FileSystemResource(SOURCE_DATA));
    }

    public void write(List<Contact> contact) {
        try {
            itemWriter.open(new ExecutionContext());
            itemWriter.write(new Chunk(contact));
            itemWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
