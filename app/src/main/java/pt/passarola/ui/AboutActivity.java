package pt.passarola.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.passarola.R;
import pt.passarola.services.dagger.DaggerableAppCompatActivity;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class AboutActivity extends DaggerableAppCompatActivity {

    private static final String FB_PASSAROLA_ID = "713214482083435";

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setupToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.about);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.button_email)
    public void onEmailButtonClick(){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("vnd.android.cursor.item/email");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"info@passarola.pt"});
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_using)));
    }

    /**
     * Load facebook page if facebook app is installed
     * open browser otherwise
     */
    @OnClick(R.id.button_facebook)
    public void onFacebookButtonClick(){
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/" + FB_PASSAROLA_ID));
            startActivity(intent);
        } catch (Exception e) {
            intent =  new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/" + FB_PASSAROLA_ID));
            startActivity(intent);
        }
    }
}
