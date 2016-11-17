package gppmds.wikilegis.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gppmds.wikilegis.dao.api.DeleteRequest;
import gppmds.wikilegis.dao.api.JSONHelper;
import gppmds.wikilegis.dao.api.PostRequest;
import gppmds.wikilegis.exception.BillException;
import gppmds.wikilegis.exception.VotesException;
import gppmds.wikilegis.model.Segment;
import gppmds.wikilegis.model.Vote;


public class VotesController {

    private static List<Vote> votesList = new ArrayList<Vote>();
    private static Context context;
    private static VotesController instance = null;

    private VotesController(final Context context) {
        this.context = context;
    }

    public static VotesController getInstance(final Context context) {
        if (instance == null) {
            instance = new VotesController(context);
        }
        return  instance;
    }
    public String registerVote(final int object_id , boolean vote) throws VotesException, JSONException {

        SharedPreferences session = PreferenceManager.getDefaultSharedPreferences(context);

        String url ="http://wikilegis-staging.labhackercd.net/api/votes/";
        JSONObject jsonObject =  new JSONObject();
        jsonObject.put("object_id" ,object_id);
        jsonObject.put("vote" , " \" " + vote + " \" ");
        jsonObject.put("token",session.getString("token",null));

        String json = "{" +
                "object_id: " +object_id+","+
                "vote: \"" + vote+"\","+
                "token: "+session.getString("token",null) +""+
                "}";

        PostRequest postRequest = new PostRequest(context, url);
        postRequest.execute(jsonObject.toString(), "application/json");
        return "SUCCESS";
    }
    public String deleteVote(final int object_id , boolean vote) throws VotesException, JSONException {

        SharedPreferences session = PreferenceManager.getDefaultSharedPreferences(context);

        String url ="http://wikilegis-staging.labhackercd.net/api/votes/";
        JSONObject jsonObject =  new JSONObject();
        jsonObject.put("object_id" ,object_id);
        jsonObject.put("vote" , " \" " + vote + " \" ");
        jsonObject.put("token",session.getString("token",null));

        String json = "{" +
                "object_id: " +object_id+","+
                "vote: \"" + vote+"\","+
                "token: "+session.getString("token",null) +""+
                "}";

        DeleteRequest deleteRequest= new DeleteRequest(context, url);
        deleteRequest.execute(jsonObject.toString(), "application/json");
        return "SUCCESS";
    }

    public boolean getVoteByUserAndIdSegment(Integer idUser, Integer idSegment, boolean vote) {
        boolean returnValue = JSONHelper.getVoteByUserAndIdSegment(idUser, idSegment, vote);

        return returnValue;
    }

    public boolean checkIfExistVote(String segmentId,boolean vote) throws BillException, VotesException, JSONException {
        SharedPreferences session = PreferenceManager.getDefaultSharedPreferences(context);

        List<Vote> voteList = new ArrayList<>();
        voteList = DataDownloadController.getVoteBySegmentId(segmentId);


    }
}


