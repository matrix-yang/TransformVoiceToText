package VoiceToText;

import com.iflytek.cloud.*;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class TransformVoiceToText extends CordovaPlugin {
    // 语音识别对象
    private SpeechRecognizer mAsr;

    private Object object;

    private StringBuffer stringBuffer;

    private CallbackContext callbackContext;

    public void init(){
        object=SpeechUtility.createUtility(this.cordova.getActivity(), SpeechConstant.APPID+"=59a8fe1c,"+SpeechConstant.FORCE_LOGIN +"=true");
        //初始化识别对象
        mAsr = SpeechRecognizer.createRecognizer(this.cordova.getActivity(), mInitListener);
    }

    /**
     * 识别监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //stringBuffer.append("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onResult(final RecognizerResult result, boolean isLast) {
            stringBuffer.append(result.getResultString());
            if (isLast){
                callbackContext.success(stringBuffer.toString());
                //showTip(stringBuffer.toString());
            }

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //stringBuffer.append("结束说话");
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //stringBuffer.append("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            callbackContext.success("onError Code：" + error.getErrorCode());
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public boolean setParam(String localism) {
        boolean result = false;
        //设置识别引擎
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置返回结果为json格式
        mAsr.setParameter(SpeechConstant.RESULT_TYPE, "plain");
        mAsr.setParameter(SpeechConstant.ACCENT, localism);
        mAsr.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
        mAsr.setParameter(SpeechConstant.ENGINE_MODE,null);
        mAsr.setParameter(SpeechConstant.SUBJECT,null);
        return result;
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                callbackContext.success("初始化失败,错误码："+code);
            }
        }
    };

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("transform")) {
            init();
            setParam(args.getString(0));
            stringBuffer =new StringBuffer();
            mAsr.startListening(mRecognizerListener);
            this.callbackContext=callbackContext;
            return true;
        }
        return false;
    }

}
