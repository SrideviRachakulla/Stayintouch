package mad.com.stayintouch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sridevi on 11/17/2015.
 */
public class UserSelectionAdapter extends ArrayAdapter<ParseUser> {
    List<ParseUser> mbjects;
    Context mcontext;
    int res;
    public ReLoad act;
    public UserSelectionAdapter(ReLoad re,Context context, int resource, List<ParseUser> objects) {
        super(context, resource, objects);
        mbjects=objects;
        mcontext=context;
        res=resource;
        act=re;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflate= (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflate.inflate(res,parent,false);
        }
        final ParseUser c=mbjects.get(position);
        CheckBox chBox=(CheckBox) convertView.findViewById(R.id.chkUser);
        chBox.setText(c.getUsername());
        chBox.setTag(c.getObjectId());
        chBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                act.AssignData("" + buttonView.getTag(), isChecked);
            }
        });
        return convertView;
    }
    public interface ReLoad
    {
        public void AssignData(String ObjectID,Boolean isChecked);
    }
}
