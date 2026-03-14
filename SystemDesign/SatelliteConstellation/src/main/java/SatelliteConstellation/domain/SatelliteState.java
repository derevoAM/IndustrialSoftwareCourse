package SatelliteConstellation.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SatelliteState {
    private boolean isActive;
    private String statusMessage;

    public SatelliteState() {
        this.isActive = false;
        this.statusMessage = "Не активирован";
    }

    public boolean activate(boolean hasSufficientPower) {
        if (hasSufficientPower && !isActive) {
            isActive = true;
            statusMessage = "Активен";
            return true;
        }
        return false;
    }

    public boolean deactivate() {
        if (isActive) {
            isActive = false;
            statusMessage = "Деактивирован";
            return true;
        }
        return false;
    }
}
