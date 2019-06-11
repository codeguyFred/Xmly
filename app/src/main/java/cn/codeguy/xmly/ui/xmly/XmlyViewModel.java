package cn.codeguy.xmly.ui.xmly;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class XmlyViewModel extends ViewModel {

    // TODO: Implement the ViewModel

    // Create a LiveData with a String
    private MutableLiveData<String> mCurrentName=new MutableLiveData<String>();

    public MutableLiveData<String> getCurrentName() {
        return mCurrentName;
    }

    public void request(){
        mCurrentName.postValue("重新获取的了"+System.currentTimeMillis());
    }
}
