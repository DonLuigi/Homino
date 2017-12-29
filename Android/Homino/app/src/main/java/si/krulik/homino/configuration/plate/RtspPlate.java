package si.krulik.homino.configuration.plate;


import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import lombok.ToString;
import si.krulik.homino.R;
import si.krulik.homino.configuration.Configuration;
import si.krulik.homino.configuration.plate.common.Plate;


@ToString (includeFieldNames = true, callSuper = true) public class RtspPlate extends Plate
{
    public RtspPlate (String id, String foregroundColor, String backgroundColor, String title, int layoutId, Configuration configuration, String rtspUrl)
    {
        super (id, title, foregroundColor, backgroundColor, null, layoutId, configuration, null);
        this.rtspUrl = rtspUrl;
    }


    @Override public void refresh ()
    {
        for (View view : getViewByPlatePageId ().values ())
        {
            VideoView videoView = ((VideoView) view.findViewById (R.id.rtspVideoView));
            MediaController mediaController;
            mediaController = new MediaController(videoView.getContext ());
            mediaController.setMediaPlayer((MediaController.MediaPlayerControl)videoView);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            mediaController.show();

            videoView.setVideoURI(Uri.parse(rtspUrl));
            videoView.requestFocus();
            videoView.start();
        }
    }


    private String rtspUrl;
}
