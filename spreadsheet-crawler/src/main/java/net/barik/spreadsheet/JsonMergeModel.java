package net.barik.spreadsheet;

import java.io.IOException;

import org.json.JSONObject;

public class JsonMergeModel {
    private RecordIO exportIO;

    public JsonMergeModel(RecordIO exportIO) {
        this.exportIO = exportIO;
    }

    public static JSONObject merge(JSONObject copyFrom, JSONObject mergeInto, String namespace) {
        // JSONObject result = copyFrom.put(namespace, copyFrom);
        JSONObject result = mergeInto.put(namespace, copyFrom);
        return result;
    }

    public void export(String resourceKey, JSONObject json) throws IOException {
        exportIO.save(resourceKey, json.toString().getBytes("UTF-8"));
    }

    public static void main(String[] args) {
        JSONObject source = new JSONObject("{ 'name': 'Titus' }");
        JSONObject dest = new JSONObject("{ 'univ': 'NC State' }");

        System.out.println(JsonMergeModel.merge(source, dest, "Student"));
    }
}
