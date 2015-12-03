package mad.com.stayintouch;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;

/**
 * Created by sridevi on 11/13/2015.
 */
public class StarterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "jYKpaD7dks6ufpD2g38mhubbBXucaYQOvWRDhLZ8", "TrMzAWUFDVkP8hwB04qimeoJjcInbJE2KeY3E4wx");
        ParseUser.enableAutomaticUser();
    }
}
