package pl.mkazik.waveformandroid.soundfile;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class CheapSoundFactory {
    static CheapSoundFile.Factory[] sSubclassFactories = new CheapSoundFile.Factory[] {
            CheapAAC.getFactory(),
            CheapAMR.getFactory(),
            CheapMP3.getFactory(),
            CheapWAV.getFactory(),
    };
    static ArrayList<String> sSupportedExtensions = new ArrayList<>();
    static HashMap<String, CheapSoundFile.Factory> sExtensionMap = new HashMap<>();

    static {
        for (CheapSoundFile.Factory f : sSubclassFactories) {
            for (String extension : f.getSupportedExtensions()) {
                sSupportedExtensions.add(extension);
                sExtensionMap.put(extension, f);
            }
        }
    }

    /**
     * Static method to create the appropriate CheapSoundFile subclass
     * given a filename.
     *
     * TODO: make this more modular rather than hardcoding the logic
     */
    @Nullable
    public static CheapSoundFile create(
            String fileName,
            CheapSoundFile.ProgressListener progressListener
    ) throws java.io.IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            throw new java.io.FileNotFoundException(fileName);
        }
        String name = f.getName().toLowerCase();
        String[] components = name.split("\\.");
        if (components.length < 2) {
            return null;
        }
        CheapSoundFile.Factory factory = sExtensionMap.get(components[components.length - 1]);
        if (factory == null) {
            return null;
        }
        CheapSoundFile soundFile = factory.create();
        soundFile.setProgressListener(progressListener);
        soundFile.ReadFile(f);
        return soundFile;
    }
}
