/**
 * Created by nifras on 1/10/17.
 */
public class Constants {
    public static final String SUCCESS = "0"; // – request is successful, no nodes in the system
    public static final String SUCESSFULL = "1";// or 2 – request is successful, 1 or 2 nodes' contacts will be returned
    public static final  String SUCESSFULL2 = "2";
    public static final  String FAILED_COMMAND = "9999"; // – failed, there is some error in the command
    public static final  String FAILED_ALREADY_REGISTERED_YOURSELF = "9998";// – failed, already registered to you, unregister first
    public static final  String FAILED_ANOTHER_USER_REGISTERED =  "9997";// – failed, registered to another user, try a different IP and port
    public static final  String FAILED_BS_FULL = "9996"; // – failed, can’t register. BS full.
    public static final  String REGOK = "REGOK";
    public static final  String JOINOK = "JOINOK";
    public static final  String LEAVEOK = "LEAVEOK";
    public static final  String SEROK = "SEROK";
    public static final  String UNROK = "UNROK";
    public static final  String SER = "SER";


    public static String ERROR = "ERROR";
    public static String JOIN = "JOIN";
    public static String LEAVE = "LEAVE";
    private static  String [] temp= ReadFile.getBootStrapSever();
    public static String BOOTSTRAP_SERVER_IP = temp[0];//IPLIKE + ".8.102";
    public static final String IPLIKE = temp[1];

}
