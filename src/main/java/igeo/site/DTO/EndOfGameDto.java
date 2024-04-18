package igeo.site.DTO;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class EndOfGameDto implements Serializable {
    private String name;
    private int level;
    private int exp;
    private int nextExp;
    @Builder
    public EndOfGameDto(String name, int level, int exp, int nextExp) {
        this.name = name;
        this.level = level;
        this.exp = exp;
        this.nextExp = nextExp;
    }
}
