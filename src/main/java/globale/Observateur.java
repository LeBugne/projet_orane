package globale;

import java.io.IOException;

public interface Observateur {

    public abstract void reagir() throws IOException;
}
