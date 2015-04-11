package microblog.model;


public class TimeHelper {
	
	public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;
    
    private TimeHelper() {
    }
    
	/**
	 * @param duration duration between time now and the past date
	 * @param pubDate The date that you want to calculate time ago of..
	 * @return a Date ex. 2 minutes ago, 5 hours ago, today, or date string format dd/MM/yyyy HH:mm
	 **/ 
    public static String millisToLongDHMS(long duration, String pubDate) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        // Check if duration more than 1000 Mills
        if (duration >= ONE_SECOND) {
        	// if temp bigger than zero that means we have to diplay the pubDate, otherwise duration < ONE_DAY 
          temp = duration / ONE_DAY;
          if (temp > 0) {
            duration -= temp * ONE_DAY;
            res.append(pubDate);
            return res.toString();
          }

          // if temp > 0 that means at least 1 hour passed
          temp = duration / ONE_HOUR;
          if (temp > 0) {
            duration -= temp * ONE_HOUR;
            res.append("today, ").append(temp).append(" hour").append(temp > 1 ? "s ago" : " ago");
            return res.toString();
          }

       // if temp > 0 that means at least 1 Minute passed and not more than 1 hour since we reached here
          temp = duration / ONE_MINUTE;
          if (temp > 0) {
            duration -= temp * ONE_MINUTE;
            res.append(temp).append(" minute").append(temp > 1 ? "s ago" : " ago");
            return res.toString();
          }

       // if temp > 0 that means at least 1 second passed and not more than 1 minute since we reached here.
          temp = duration / ONE_SECOND;
          if (temp > 0) {
            res.append(temp).append(" second").append(temp > 1 ? "s ago" : " ago");
          }
          return res.toString();
        } else {
          return "0 second";
        }
      }

}
