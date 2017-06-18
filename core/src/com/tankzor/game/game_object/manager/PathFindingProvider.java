package com.tankzor.game.game_object.manager;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;

/**
 * Created by Admin on 5/22/2017.
 */

public interface PathFindingProvider {
    AStarGridFinder<GridCell> getPathFinder();
    NavigationGrid<GridCell> getNavigationGridIgnoreSomeWall();
    NavigationGrid<GridCell> getNavigationGrid();
}
