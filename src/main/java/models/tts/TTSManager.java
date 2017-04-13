package models.tts;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * Created by Kevin O'Brien on 4/11/2017.
 */
public class TTSManager {
    private static final String VOICE_NAME = "kevin16";

    private final Voice voice;

    public TTSManager() {
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICE_NAME);
        voice.allocate();
    }

    public void speak(String s) {
        if (s != null && !s.isEmpty()) {
            voice.speak(s);
        }
    }

}
