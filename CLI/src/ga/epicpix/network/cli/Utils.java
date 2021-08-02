package ga.epicpix.network.cli;

import ga.epicpix.network.common.Reflection;

import java.util.Collection;

import static ga.epicpix.network.common.CommonUtils.replaceAt;

public class Utils {

    public static class StringInfo {
        public int len;
        public String str;

        public StringInfo(int len, String str) {
            this.len = len;
            this.str = str;
        }
    }

    public interface Compute<T> {
        Object compute(T data);
    }

    public static String setStrings(String repl, StringInfo... strings) {
        int current = 1;
        for(StringInfo str : strings) {
            repl = replaceAt(repl, current+(str.len-1)/2-(str.str.length()-1)/2, str.str);
            current += str.len;
            current++;
        }
        return repl;
    }

    public static <T> int getLongestFromCompute(int def, Compute<T> compute, Collection<? extends T> objs) {
        for(T obj : objs) {
            Object ret = compute.compute(obj);
            if(def < ret.toString().length()) def = ret.toString().length();
        }
        return def;
    }

    public static String getParam(String[] args, int arg) {
        return args.length > arg ? args[arg] : null;
    }

    public static String convertTime(long timems) {
        boolean neg = timems<0;
        timems = Math.abs(timems);
        long sec = (timems/1000)%60;
        long min = (timems/60000)%60;
        long hr = (timems/3600000)%24;
        long d = timems/86400000;
        if(d!=0) return (neg?"-":"") + d + "d " + hr + "h " + min + "m " + sec + "s";
        else if(hr!=0) return (neg?"-":"") + hr + "h " + min + "m " + sec + "s";
        else if(min!=0) return (neg?"-":"") + min + "m " + sec + "s";
        else return (neg?"-":"") + sec + "s";
    }

}
