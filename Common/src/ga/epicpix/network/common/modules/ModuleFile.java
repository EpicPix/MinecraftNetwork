package ga.epicpix.network.common.modules;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleFile {

    private ModuleData data;
    private List<ModuleFileData> fileData = new ArrayList<>();

    public static class ModuleFileData {

        private ModuleFileData() {}

        public String filename;
        public byte[] data;

    }

    public ModuleFile(byte[] in) throws IOException {
        DataInputStream din = new DataInputStream(new ByteArrayInputStream(in));
        String moduleJsonStr = new String(din.readNBytes(din.readUnsignedShort()));
        data = new Gson().fromJson(moduleJsonStr, ModuleData.class);
        int files = din.readUnsignedShort();
        for(int i = 0; i<files; i++) {
            ModuleFileData fdata = new ModuleFileData();
            fdata.filename = new String(din.readNBytes(din.readUnsignedShort()));
            fdata.data = din.readNBytes(din.readInt());
            fileData.add(fdata);
        }
        din.close();
    }

    public ModuleData getData() {
        return data;
    }

    public byte[] getFileData(String file) {
        for(ModuleFileData f : fileData) {
            if(f.filename.equals(file)) {
                return f.data;
            }
        }
        return null;
    }

    protected void clear() {
        data = null;
        fileData.clear();
        fileData = null;
    }
}
