// Generated code from Butter Knife. Do not modify!
package kingsley.www.runsong.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import kingsley.www.runsong.R;

public class MusicNetFragment_ViewBinding implements Unbinder {
  private MusicNetFragment target;

  @UiThread
  public MusicNetFragment_ViewBinding(MusicNetFragment target, View source) {
    this.target = target;

    target.mNetMusicRecyclerView = Utils.findRequiredViewAsType(source, R.id.netMusicRecyclerView, "field 'mNetMusicRecyclerView'", RecyclerView.class);
    target.mMetMusicLoading = Utils.findRequiredViewAsType(source, R.id.netMusic_loading, "field 'mMetMusicLoading'", LinearLayout.class);
    target.mNetMusicLoadFail = Utils.findRequiredViewAsType(source, R.id.netMusic_load_fail, "field 'mNetMusicLoadFail'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MusicNetFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mNetMusicRecyclerView = null;
    target.mMetMusicLoading = null;
    target.mNetMusicLoadFail = null;
  }
}
