package ir.behizz.openspotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoRuntimeSettings;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // create config file if not exists
        createGeckoViewConfigFile();

        // init geckoview
        GeckoSession session = initGeckoView();

        // load Spotify
        session.loadUri("https://open.spotify.com");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Toast.makeText(this, "Created by behizz\nbehizz@gmail.com",
                        Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void createGeckoViewConfigFile() {
        byte[] configData = new byte[0];
        try {
            InputStream resourceFis = getResources().openRawResource(R.raw.geckoviewconfig);
            configData = new byte[resourceFis.available()];
            resourceFis.read(configData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File configFile = new File(this.getCacheDir(), getResources().getString(R.string.geckoview_config_file_name));
        if (!configFile.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(configFile);
                fos.write(configData);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private GeckoSession initGeckoView() {
        GeckoRuntimeSettings runtimeSettings = new GeckoRuntimeSettings.Builder()
                .configFilePath(this.getCacheDir() + "/" + getResources().getString(R.string.geckoview_config_file_name))
                .build();
        GeckoView view = findViewById(R.id.geckoview);
        GeckoSession session = new GeckoSession();
        GeckoRuntime runtime = GeckoRuntime.create(this, runtimeSettings);
        runtime.getSettings().setJavaScriptEnabled(true);
        session.getSettings().setAllowJavascript(true);
        session.open(runtime);
        view.setSession(session);

        return session;
    }

}
