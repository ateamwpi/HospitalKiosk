package models.path.algo;

import models.path.Node;
import models.path.Path;

/**
 * Created by mattm on 4/3/2017.
 */
public interface IPathfindingAlgorithm {

    public Path findPath(Node start, Node goal);
}
