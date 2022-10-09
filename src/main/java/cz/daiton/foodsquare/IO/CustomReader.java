package cz.daiton.foodsquare.IO;

import cz.daiton.foodsquare.ingredient.Ingredient;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class CustomReader extends FlatFileItemReader<Ingredient> implements ItemReader<Ingredient> {

    public CustomReader() {
        setResource(new FileSystemResource("src/main/resources/input/data.csv"));
        setLinesToSkip(1);
        setLineMapper(getDefaultLineMapper());
        setEncoding("Cp1252");
    }

    public DefaultLineMapper<Ingredient> getDefaultLineMapper() {
        DefaultLineMapper<Ingredient> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(";");
        delimitedLineTokenizer.setNames(
                "code",
                "name",
                "calories",
                "fat",
                "saturatedFattyAcids",
                "monounsaturatedFattyAcids",
                "polyunsaturatedFattyAcids",
                "transFattyAcids",
                "carbohydrateTotal",
                "getCarbohydrateAvailable",
                "sugar",
                "fibre",
                "protein",
                "sodium",
                "salt",
                "water");
        delimitedLineTokenizer.setIncludedFields(0, 2, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 21, 22, 23);
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        BeanWrapperFieldSetMapper<Ingredient> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(Ingredient.class);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        return defaultLineMapper;
    }
}
