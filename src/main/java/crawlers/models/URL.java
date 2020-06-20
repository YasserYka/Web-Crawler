package crawlers.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class URL {

	private String url;
	private Date createdAt;
	private String DomainName;
	
}
