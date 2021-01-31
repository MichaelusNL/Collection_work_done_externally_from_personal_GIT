import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

interface AlertDAO{
    public UUID addAlert(Date time);
    public Date getAlert(UUID id);
}

class AlertService {
    private final AlertDAO storage = new AlertDAO() {
        @Override
        public UUID addAlert(Date time) {
            return null;
        }

        @Override
        public Date getAlert(UUID id) {
            return null;
        }
    };
    private AlertDAO obj;
    public AlertService (AlertDAO storage){
        this.obj=storage;
    }

    public UUID raiseAlert() {
        return this.obj.addAlert(new Date());
    }

    public Date getAlertTime(UUID id) {
        return this.obj.getAlert(id);
    }
}

class MapAlertDAO implements AlertDAO{
    private final Map<UUID, Date> alerts = new HashMap<UUID, Date>();

    public UUID addAlert(Date time) {
        UUID id = UUID.randomUUID();
        this.alerts.put(id, time);
        return id;
    }

    public Date getAlert(UUID id) {
        return this.alerts.get(id);
    }
}