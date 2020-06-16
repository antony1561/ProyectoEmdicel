package com.example.proyectoemdicel.models;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyectoemdicel.MainActivity;
import com.example.proyectoemdicel.helpers.QueueUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Producto {
    public String phone;
    public String nickname;
    public String urlImage;
    public Producto(String _phone, String _nickname, String _urlImage) {
        this.phone = _phone;
        this.nickname = _nickname;
        this.urlImage = _urlImage;
    }

    public static ArrayList getCollection() {
        ArrayList<Producto> collection = new ArrayList<>();
        collection.add(new Producto("981999923", "Bichito",""));
        collection.add(new Producto("9859913923", "Plaga", ""));
        collection.add(new Producto("981914213", "Libelula", ""));
        return collection;
    }
    public static void injectContactsFromCloud(final QueueUtils.QueueObject o,
                                               final ArrayList<Producto> contactos,
                                               final MainActivity _interface) {
        String url = "http://fipo.equisd.com/api/users.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("objects")) {

                            try {
                                JSONArray list = response.getJSONArray("objects");
                                for (int i=0; i < list.length(); i++) {
                                    JSONObject o = list.getJSONObject(i);
                                    contactos.add(new Producto(o.getString("first_name"),
                                            o.getString("last_name"), o.getString("avatar")));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            _interface.refreshList(); // Esta función debemos implementarla
                            // en nuestro activity
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        o.addToRequestQueue(jsonObjectRequest);
    }

}
