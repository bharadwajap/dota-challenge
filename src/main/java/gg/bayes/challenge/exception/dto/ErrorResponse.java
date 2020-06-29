package gg.bayes.challenge.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * Response class for exceptions
 *
 * @author Bharadwaj Adepu
 * @since 1.0-SNAPSHOT
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

	public ErrorResponse(String title, String detail){
		this.title = title;
		this.detail = detail;
	}
    private final String title;
    private final String detail;
    private String instance;
    private Integer status;

}
