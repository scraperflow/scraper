package scraper.nodes.test;

import scraper.annotations.NotNull;
import scraper.annotations.node.FlowKey;
import scraper.annotations.node.NodePlugin;
import scraper.api.exceptions.NodeException;
import scraper.api.flow.FlowMap;
import scraper.api.node.container.FunctionalNodeContainer;
import scraper.api.node.type.FunctionalNode;
import scraper.api.template.T;

/**
 * @author Albert Schimpf
 */
@NodePlugin(value = "0.0.1", deprecated = true)
public final class IntConsumer implements FunctionalNode {

    @FlowKey
    private T<Integer> input = new T<>(){};

    @Override
    public void modify(@NotNull FunctionalNodeContainer n, @NotNull FlowMap o) throws NodeException {
        // do nothing as pure as it gets
    }
}
