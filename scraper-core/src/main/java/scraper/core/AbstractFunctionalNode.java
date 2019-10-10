package scraper.core;

import scraper.annotations.NotNull;
import scraper.annotations.node.NodePlugin;
import scraper.api.exceptions.NodeException;
import scraper.api.flow.FlowMap;
import scraper.api.node.type.FunctionalNode;


/**
 * Fixes accept method for functional nodes
 */
@NodePlugin("1.0.0")
public abstract class AbstractFunctionalNode extends AbstractNode implements FunctionalNode {
    @NotNull
    @Override
    public FlowMap process(@NotNull FlowMap o) throws NodeException {
        modify(o);
        return forward(o);
    }
}
