package Skillbox;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class ContactsReader {
    private FlatFileItemReader<Contact> itemReader = new FlatFileItemReader<>();
    private DefaultLineMapper<Contact> lineMapper = new DefaultLineMapper<>();
    private ContactFieldSetMapper contactFieldSetMapper;
    @Value("${app.data.source}")
    private String SOURCE_DATA;

    @Autowired
    public ContactsReader(ContactFieldSetMapper contactFieldSetMapper) {
        this.contactFieldSetMapper = contactFieldSetMapper;
    }

    @PostConstruct
    private void init(){
        this.itemReader.setResource(new FileSystemResource(SOURCE_DATA));
        this.lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
        this.lineMapper.setFieldSetMapper(contactFieldSetMapper);
        this.itemReader.setLineMapper(lineMapper);
        this.itemReader.open(new ExecutionContext());
    }

    public Contact read() throws Exception {
        return itemReader.read();
    }

    public void reset(){
        itemReader.close();
        itemReader.open(new ExecutionContext());
    }

    public class FlatFileWriter {

        private boolean useBuilder = true;

        public ItemWriter<Contact> flatFileWriter() {
            BeanWrapperFieldExtractor<Contact> fieldExtractor = new BeanWrapperFieldExtractor<>();
            fieldExtractor.setNames(new String[] { "siteId", "date", "temperature" }); //Set mapping field
            fieldExtractor.afterPropertiesSet(); //Parameter check

            DelimitedLineAggregator<Contact> lineAggregator = new DelimitedLineAggregator<>();
            lineAggregator.setDelimiter(","); //Set output separator
            lineAggregator.setFieldExtractor(fieldExtractor); //Setting up the FieldExtractor processor

            FlatFileItemWriter<Contact> fileWriter = new FlatFileItemWriter<>();
            fileWriter.setLineAggregator(lineAggregator);
            fileWriter.setResource(new FileSystemResource("src/main/resources/out-data.txt")); //Set output file location
            fileWriter.setName("outputData");

            if (useBuilder) {//Using builder to create
                fileWriter = new FlatFileItemWriterBuilder<Contact>().name("outputData")
                        .resource(new FileSystemResource("src/main/resources/out-data.txt")).lineAggregator(lineAggregator)
                        .build();
            }
            return fileWriter;
        }
    }
}
