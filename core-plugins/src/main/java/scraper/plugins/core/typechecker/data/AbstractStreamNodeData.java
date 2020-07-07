package scraper.plugins.core.typechecker.data;


import scraper.annotations.NotNull;
import scraper.api.node.container.NodeContainer;
import scraper.api.node.type.Node;
import scraper.api.specification.ScrapeInstance;
import scraper.core.AbstractStreamNode;
import scraper.plugins.core.flowgraph.FlowUtil;
import scraper.plugins.core.flowgraph.api.ControlFlowGraph;
import scraper.plugins.core.flowgraph.api.Version;
import scraper.plugins.core.typechecker.TypeChecker;
import scraper.plugins.core.typechecker.TypeEnvironment;
import scraper.util.TemplateUtil;

import java.util.List;
import java.util.Set;

import static java.lang.System.Logger.Level.DEBUG;
import static scraper.plugins.core.typechecker.TypeChecker.getDefaultDataFlowOutput;

@SuppressWarnings({"unused", "OptionalGetWithoutIsPresent"})
public final class AbstractStreamNodeData {
    private static final System.Logger log = System.getLogger("TypeChecker");

    @Version("0.1.0") @NotNull
    public static void infoAfter(TypeChecker checker, TypeEnvironment env, NodeContainer<? extends Node> node, ControlFlowGraph cfg,
                                 ScrapeInstance spec, List<NodeContainer<?>> visited) throws Exception {
        Boolean collect = (Boolean) FlowUtil.getFieldForClass("collect", node, AbstractStreamNode.class).get();
        if(!collect) return; // types do not change

        getDefaultDataFlowOutput(node).forEach((fieldName, output) ->
                {
                    log.log(DEBUG, "<{0}> {1} :: {2}", node.getAddress(), output.getLocation(), "java.lang.List<"+output.getTarget().getTypeString()+">");
                    checker.add(env, output.getLocation(), TemplateUtil.listOf(output.get()));
                }
        );
    }
}

