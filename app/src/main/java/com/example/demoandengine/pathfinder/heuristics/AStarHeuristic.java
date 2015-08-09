package com.example.demoandengine.pathfinder.heuristics;

import android.graphics.Point;

/**
 * Created by danhdong on 8/8/2015.
 */
public interface AStarHeuristic {

    /**
     *
     * The heuristic tries to guess how far a given Node is from the goal Node.
     * The lower the cost, the more likely a Node will be searched next.
     */
    public float getEstimatedDistanceToGoal(Point start, Point goal);
}
