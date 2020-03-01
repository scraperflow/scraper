package scraper.nodes.core.stream;

import scraper.annotations.NotNull;
import scraper.annotations.node.FlowKey;
import scraper.annotations.node.NodePlugin;
import scraper.api.exceptions.ValidationException;
import scraper.api.flow.FlowMap;
import scraper.api.node.container.NodeContainer;
import scraper.api.node.container.StreamNodeContainer;
import scraper.api.node.type.Node;
import scraper.api.node.type.StreamNode;
import scraper.api.specification.ScrapeInstance;
import scraper.api.template.L;
import scraper.api.template.T;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Applies a regex to some input content.
 *
 * Produces a list of maps.
 * The maps capture the content of the capture groups.
 * If groups are empty, the map is empty and the list is populated with empty maps.
 */
@NodePlugin("0.10.0")
public final class RegexNode implements StreamNode {

    /** Regex as a Java String */
    @FlowKey(mandatory = true)
    private String regex;

    /** The content to apply the regex on */
    @FlowKey(defaultValue = "\"{content}\"") @NotNull
    private final T<String> content = new T<>(){};

    /** Key: location of where to put the group; Value: Group number of the regex. */
    @FlowKey(defaultValue = "{}")
    private T<Map<String, Integer>> groups = new T<>(){};

    /** Default output if no matches are present */
    @FlowKey
    private T<Map<String, ?>> noMatchDefaultOutput = new T<>(){};

    /** Where the output list will be put. If there's already a list at that key, it will be replaced. */
    @FlowKey(defaultValue = "\"output\"")
    private L<Map<String, ?>> output = new L<>(){};

    /** Pattern dotall option */
    @FlowKey(defaultValue = "\"true\"")
    private Boolean dotAll;

    // compiles the regex pattern
    private Pattern p;

    @Override
    public void init(@NotNull NodeContainer<? extends Node> n, final @NotNull ScrapeInstance job) throws ValidationException {
        try {
            if(dotAll) p = Pattern.compile(regex, Pattern.DOTALL);
            else p = Pattern.compile(regex);
        } catch (Exception e) { throw new ValidationException(e, "Failed to compile pattern."); }
    }

    @Override
    public void process(@NotNull StreamNodeContainer n, @NotNull FlowMap o) {
        n.collect(o, List.of(o.eval(output)));

        String content = o.eval(this.content);
        Map<String, Integer> groups = o.evalIdentity(this.groups);

        Matcher m = p.matcher(content);

        // match regex until no matches found
        while (m.find()) {
            Map<String, Object> singleCapture = new HashMap<>(groups.keySet().size());
            for (String name : groups.keySet()) {
                Integer group = groups.get(name);
                singleCapture.put(name, m.group(group));
            }

            FlowMap copy = o.copy();
            copy.output(output, singleCapture);
            n.streamFlowMap(o, copy);
        }

        Optional<Map<String, ?>> evalDefault = o.evalMaybe(noMatchDefaultOutput);
        if(evalDefault.isPresent() && m.reset().results().findAny().isEmpty()) {
            FlowMap copy = o.copy();
            copy.output(output, evalDefault.get());
            n.streamFlowMap(o, copy);
        }
    }
}
