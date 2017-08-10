package com.dev.nora.support.wizard.loader;

import com.dev.nora.support.wizard.item.APIItem;
import com.dev.nora.support.wizard.logger.Logger;
import com.dev.nora.support.wizard.util.Formatter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public class DataLoader {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final int DEF_TIMEOUT = 10000;
    public static final String TAG = DataLoader.class.getSimpleName();

    public static <T> T loadData(String link, Type type) {
        return loadData(link, type, DEF_TIMEOUT, true);
    }

    public static <T> T loadData(String link, Type type, int timeOut, boolean useCache) {
        if (link == null || type == null) {
            return null;
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        Logger.i(TAG, link);
        try {
            long start = System.currentTimeMillis();
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeOut);
            connection.setReadTimeout(timeOut);
            connection.setUseCaches(useCache);
            connection.setDefaultUseCaches(useCache);
            int rspCode = connection.getResponseCode();
            if (rspCode != HttpURLConnection.HTTP_OK) {
                Logger.e(TAG, "loadData: Failed. RSP code: " + rspCode);
                return null;
            }
            String line = null;
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Logger.i(TAG, link + " Load  time: " +
                    (System.currentTimeMillis() - start) + " ms");
            start = System.currentTimeMillis();
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new StringReader(builder.toString()));
            jsonReader.setLenient(true);
            T t = gson.fromJson(jsonReader, type);
            if (t != null) {
                resolveData(t);
            }
            Logger.i(TAG, link + " Parse JSON  time: " +
                    (System.currentTimeMillis() - start) + " ms");
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static <T> T loadLocalData(String localPath, Type type) {
        BufferedReader reader = null;

        try {
            Logger.i(TAG, "loadLocalData: " + localPath);
            FileInputStream e = new FileInputStream(localPath);
            StringBuilder builder = new StringBuilder();
            String line = null;
            reader = new BufferedReader(new InputStreamReader(e));

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new StringReader(builder.toString()));
            jsonReader.setLenient(true);
            return gson.fromJson(jsonReader, type);
        } catch (JsonSyntaxException | IOException var18) {
            var18.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }

        return null;
    }

    public static void saveStream(String outPath, InputStreamReader inputReader) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outPath));
        BufferedReader reader = new BufferedReader(inputReader);

        String line;
        while ((line = reader.readLine()) != null) {
            bufferedWriter.write(line);
        }

        bufferedWriter.flush();
        bufferedWriter.close();
        reader.close();
    }

    public static <T> T loadFromStream(InputStream path, Type type) {
        try {
            Gson je = new Gson();
            JsonReader jsonReader = new JsonReader(new InputStreamReader(path));
            jsonReader.setLenient(true);
            return je.fromJson(jsonReader, type);
        } catch (JsonSyntaxException var4) {
            var4.printStackTrace();
            return null;
        }
    }


    public static <T> T postData(String link, Map<String, String> requestProperties,
                                 String payload, Type type, int timeOut) {
        if (link != null && link.length() >= 1) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            Logger.i(TAG, "postData: " + link);
            T var12;
            try {
                URL url = new URL(link);
                connection = (HttpURLConnection) url.openConnection();
                Map.Entry line;
                if (requestProperties != null && requestProperties.size() > 0) {
                    Set rspCode = requestProperties.entrySet();
                    for (Object aRspCode : rspCode) {
                        line = (Map.Entry) aRspCode;
                        connection.setRequestProperty((String) line.getKey(), (String) line.getValue());
                    }
                }
                connection.setDoOutput(true);
                connection.setRequestMethod(POST);
                connection.setConnectTimeout(timeOut);
                //connection.setReadTimeout(timeOut);
                if (payload != null) {
                    OutputStream rspCode1 = connection.getOutputStream();
                    BufferedWriter builder1 = new BufferedWriter(new OutputStreamWriter(rspCode1, "UTF-8"));
                    Logger.e("xxx", "postData payload: " + payload);
                    builder1.write(payload);
                    builder1.flush();
                    builder1.close();
                    rspCode1.close();
                }
                int rspCode2 = connection.getResponseCode();
                if (rspCode2 != 200) {
                    Logger.i(TAG, "postData: srp code: " + rspCode2);
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                StringBuilder builder2 = new StringBuilder();
                line = null;

                String line1;
                while ((line1 = reader.readLine()) != null) {
                    builder2.append(line1);
                }

                if (builder2.length() <= 0) {
                    return null;
                }

                Logger.i(TAG, "postData: " + builder2.toString());
                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(new StringReader(builder2.toString()));
                jsonReader.setLenient(true);
                var12 = gson.fromJson(jsonReader, type);
            } catch (JsonSyntaxException | IOException ex) {
                ex.printStackTrace();
                return null;
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var22) {
                        var22.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
            return var12;
        }
        return null;
    }

    public static <T> T postData(String link, Map<String, String> requestProperties,
                                 Map<String, String> payload, Type type, int timeOut) {
        try {
            return postData(link, requestProperties, Formatter.mapToString(payload), type, timeOut);
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }
        return null;
    }

    private static <T> void resolveData(Object obj) {
        if (obj instanceof APIItem) {
            ((APIItem) obj).resolveData();
        }
    }

    public static class Builder {
        private String mLink;
        private Type mObjectType;
        private boolean mUseCache;
        private int mTimeOut = DEF_TIMEOUT;
        private boolean mUsePostMethod = false;
        private Map<String, String> mRequestProperties;
        private HashMap<String, String> mPayload;
        private String mPayloadString;

        public Builder() {
        }

        public Builder(Type clazz) {
            this.mObjectType = clazz;
        }

        public DataLoader.Builder setLink(String link) {
            this.mLink = link;
            return this;
        }

        public DataLoader.Builder setObjectType(Type type) {
            this.mObjectType = type;
            return this;
        }

        public DataLoader.Builder setUseCache(boolean useCache) {
            this.mUseCache = useCache;
            return this;
        }

        public DataLoader.Builder setUserAgent(String userAgent) {
            if (userAgent != null) {
                if (this.mRequestProperties == null) {
                    this.mRequestProperties = new HashMap<>();
                }

                this.mRequestProperties.put("User-Agent", userAgent);
            }

            return this;
        }

        public DataLoader.Builder setTimeOut(int timeOut) {
            this.mTimeOut = timeOut;
            return this;
        }

        public DataLoader.Builder setUsePost(HashMap<String, String> payload) {
            this.mUsePostMethod = true;
            this.mPayload = payload;
            return this;
        }

        public DataLoader.Builder setPayloadString(String payload) {
            this.mUsePostMethod = true;
            this.mPayloadString = payload;
            this.mPayload = null;
            return this;
        }

        public DataLoader.Builder addRequestProperty(String key, String value) {
            if (key == null) {
                return this;
            } else {
                if (this.mRequestProperties == null) {
                    this.mRequestProperties = new HashMap<>();
                }

                this.mRequestProperties.put(key, value);
                return this;
            }
        }

        public DataLoader.Builder setRequestProperties(Map<String, String> prs) {
            this.mRequestProperties = prs;
            return this;
        }

        public <T> T ok() {
            T data = DataLoader.loadData(this.mLink, this.mObjectType, this.mTimeOut, this.mUseCache);
            if (this.mUsePostMethod) {
                data = DataLoader.postData(this.mLink, this.mRequestProperties, this.mPayload, this.mObjectType, this.mTimeOut);
                if (this.mPayloadString != null) {
                    data = DataLoader.postData(this.mLink, this.mRequestProperties, this.mPayloadString, this.mObjectType, this.mTimeOut);
                }
            }
            return data;
        }
    }
}
