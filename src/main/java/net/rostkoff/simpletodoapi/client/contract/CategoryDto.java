package net.rostkoff.simpletodoapi.client.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private Long id;
    private String title;
    private String colour;
}
