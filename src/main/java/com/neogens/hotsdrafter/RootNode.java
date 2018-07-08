
package com.neogens.hotsdrafter;

/**
 *
 * @author François Gourdeau
 */

public class RootNode extends Node{
     public RootNode(DraftTree tree) {
        level = ROOT;
        this.move = Hero.OPEN;
        //state = new State(tree);
        this.tree=tree;
    }
    /*
    Node getBestMove(State targetState)  {
        Node node = this;
        Node child;
        while (node.level < targetState.moves.length && targetState.moves[node.level+1] != Hero.OPEN) {
            int choosenMove = targetState.moves[node.level+1];
            if(node.children.containsKey(choosenMove)){
                child = node.children.get(choosenMove);
           }else{
               child = createNode(node, tree, targetState, choosenMove, node.level+1);
               node.children.put(choosenMove, child);
            }
           node=child;
          }
        if (node.level < targetState.moves.length) {
            Node nodeRes = node.getBestMove(targetState);
            if (nodeRes==null){
                return node;
            }
            node=nodeRes;
        }
        return node;
    }
    */
    public String getLevelSeq() {
            return "Racine";
    }
    public String getHero() {
        return "-";
    }

  
}
