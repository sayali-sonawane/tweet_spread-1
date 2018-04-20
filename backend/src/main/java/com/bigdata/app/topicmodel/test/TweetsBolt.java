package com.bigdata.app.topicmodel.test;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class TweetsBolt extends BaseRichBolt implements Serializable {

    private OutputCollector collector;
    private final int NUM_TWEET = 500;
    private int countTweet = 0;
    private List<String> tweets = new ArrayList<String>();

    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(Tuple input) {
        try {
            String tweet = input.getString(0);
            tweets.add(tweet);
            countTweet++;
            if (countTweet == NUM_TWEET) {
                collector.emit("topic-stream", new Values(tweets));
            }
            this.collector.ack(input);
        } catch (Exception exception) {
            exception.printStackTrace();
            this.collector.fail(input);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("topic-stream", new Fields("tweets"));
    }

}