import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Id
    private long id;
    private String label;
    private String manufacturer;
    private double price;
    private double length;
    private double width;
    private double weight;
    private String color;
    private String category;
    @JsonProperty("manufacturing_date")
    private Instant manufacturingDate;
}


//Key Value Store