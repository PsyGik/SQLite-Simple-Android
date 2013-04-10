package garin.artemiy.sqlitesimple.library.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Artemiy Garin
 * Copyright (C) 2013 SQLite Simple Project
 * *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * *
 * http://www.apache.org/licenses/LICENSE-2.0
 * *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SimplePreferencesUtil {

    private SharedPreferences.Editor sharedPreferencesEditor;
    private SharedPreferences sharedPreferences;

    public SimplePreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(SimpleConstants.SHARED_PREFERENCES_DATABASE,
                Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public void clearAllPreferences(int databaseVersion) {
        sharedPreferencesEditor.clear();
        putDatabaseVersion(databaseVersion);
        sharedPreferencesEditor.commit();
    }


    private int getNextIndex(String place) {
        return getCurrentIndex(place) + 1;
    }

    private void putCurrentIndex(String place, int index) {
        sharedPreferencesEditor.putInt(String.format(SimpleConstants.SHARED_PREFERENCES_INDEX, place), index);
        sharedPreferencesEditor.commit();
    }

    private int getCurrentIndex(String place) {
        return sharedPreferences.getInt(String.format(SimpleConstants.SHARED_PREFERENCES_INDEX, place), 1);
    }

    public void putList(String place, List<String> entityList) {
        for (String entity : entityList) {
            int index = getNextIndex(place);
            sharedPreferencesEditor.putString(String.format(SimpleConstants.SHARED_PREFERENCES_LIST, place, index), entity);
            putCurrentIndex(place, index);
        }
    }

    public List<String> getList(String place) {
        List<String> resultList = new ArrayList<String>();

        for (int i = 1; i <= getCurrentIndex(place); i++) {
            String savedString =
                    sharedPreferences.getString(String.format(SimpleConstants.SHARED_PREFERENCES_LIST, place, i), null);
            if (savedString != null) {
                resultList.add(savedString);
            }
        }

        return resultList;
    }

    public void commit() {
        sharedPreferencesEditor.commit();
    }

    public void putDatabaseVersion(int databaseVersion) {
        sharedPreferencesEditor.putInt(SimpleConstants.SHARED_DATABASE_VERSION, databaseVersion);
    }

    public int getDatabaseVersion() {
        // if not found, return first version -> 1
        return sharedPreferences.getInt(SimpleConstants.SHARED_DATABASE_VERSION, SimpleConstants.FIRST_DATABASE_VERSION);
    }

    public boolean isVirtualTableCreated() {
        return sharedPreferences.getBoolean(SimpleConstants.SHARED_DATABASE_VIRTUAL_TABLE_CREATED, false);
    }

    public void setVirtualTableCreated() {
        sharedPreferencesEditor.putBoolean(SimpleConstants.SHARED_DATABASE_VIRTUAL_TABLE_CREATED, true);
        sharedPreferencesEditor.commit();
    }

    public void setVirtualTableDropped() {
        sharedPreferencesEditor.putBoolean(SimpleConstants.SHARED_DATABASE_VIRTUAL_TABLE_CREATED, false);
        sharedPreferencesEditor.commit();
    }

}
