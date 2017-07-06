package mock.core;

import java.util.HashMap;
import java.util.Map;

public class DnsSdTxtRecord {

    public String fullDomainName = "";
    public Map<String, String> txtRecordMap = new HashMap<String, String>();
    public Device srcDevice = new Device();
}
