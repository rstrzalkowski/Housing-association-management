package pl.lodz.p.it.ssbd2023.ssbd05.exceptions.conflict;

import pl.lodz.p.it.ssbd2023.ssbd05.exceptions.AppConflictException;
import pl.lodz.p.it.ssbd2023.ssbd05.utils.I18n;

public class InactiveMeterException extends AppConflictException {
    public InactiveMeterException() {
        super(I18n.INACTIVE_METER);
    }
}
