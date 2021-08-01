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

}
