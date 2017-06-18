package com.tankzor.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Created by alwex on 28/05/2015.
 */
public class QuadTree<T> {

    // the current nodes
    public Array<QuadNode<T>> nodes;
    public QuadTree<T> parent;

    // current rectangle zone
    private QuadRectangle zone;

    // GLOBAL CONFIGURATION
    // if this is reached,
    // the zone is subdivided
    public static int maxItemByNode = 2;
    public static int maxLevel = 5;

    int level;

    // the four sub regions,
    // may be null if not needed
    QuadTree<T>[] regions;

    public static final int REGION_SELF = -1;
    public static final int REGION_NW = 0;
    public static final int REGION_NE = 1;
    public static final int REGION_SW = 2;
    public static final int REGION_SE = 3;

    public QuadTree(QuadRectangle definition, int level) {
        this.zone = definition;
        this.nodes = new Array<QuadNode<T>>();
        this.level = level;
        this.parent = null;
    }

    public QuadTree(QuadRectangle definition, int level, QuadTree<T> parent) {
        this.zone = definition;
        this.nodes = new Array<QuadNode<T>>();
        this.level = level;
        this.parent = parent;
    }

    public QuadRectangle getZone() {
        return this.zone;
    }

    public QuadNode<T> getQuadNode(int index) {
        return nodes.get(index);
    }

    public QuadNode<T> removeQuadNode(int index) {
        return nodes.removeIndex(index);
    }

    private int findRegion(QuadRectangle r, boolean split) {
        int region = REGION_SELF;
        if (nodes.size >= maxItemByNode && this.level < maxLevel) {
            // we don't want to split if we just need to retrieve
            // the region, not inserting an element
            if (regions == null && split) {
                // then create the subregions
                this.split();
                //and redispatch all nodes inside
                redispatchQuadTree();
            }
        }

        if (regions != null) {
            if (regions[REGION_NW].getZone().contains(r)) {
                region = REGION_NW;
            } else if (regions[REGION_NE].getZone().contains(r)) {
                region = REGION_NE;
            } else if (regions[REGION_SW].getZone().contains(r)) {
                region = REGION_SW;
            } else if (regions[REGION_SE].getZone().contains(r)) {
                region = REGION_SE;
            }
        }

        return region;
    }

    public void clear() {
        if (regions != null) {
            for (int i = 0; i < 4; i++) {
                regions[i].clear();
            }
        }
        nodes.clear();
    }

    private void split() {
        regions = new QuadTree[4];

        float newWidth = zone.width / 2;
        float newHeight = zone.height / 2;
        int newLevel = level + 1;

        regions[REGION_NW] = new QuadTree<T>(new QuadRectangle(zone.x, zone.y + zone.height / 2, newWidth, newHeight),
                newLevel,
                this);

        regions[REGION_NE] = new QuadTree<T>(new QuadRectangle(zone.x + zone.width / 2, zone.y + zone.height / 2, newWidth, newHeight),
                newLevel,
                this);

        regions[REGION_SW] = new QuadTree<T>(new QuadRectangle(zone.x, zone.y, newWidth, newHeight),
                newLevel,
                this);

        regions[REGION_SE] = new QuadTree<T>(new QuadRectangle(zone.x + zone.width / 2, zone.y, newWidth, newHeight),
                newLevel,
                this);
    }

    public void insert(QuadRectangle r, T element) {
        int region = this.findRegion(r, true);
        if (region == REGION_SELF || this.level == maxLevel) {
            nodes.add(new QuadNode<T>(r, element));
        } else {
            regions[region].insert(r, element);
        }
    }

    private void redispatchQuadTree() {
        Array<QuadNode<T>> tempNodes = new Array<QuadNode<T>>(nodes);
        nodes.clear();

        for (QuadNode<T> node : tempNodes) {
            this.insert(node.r, node.element);
        }
    }

    public Array<T> getElements(Array<T> list, QuadRectangle r) {
        int region = this.findRegion(r, false);

        int length = nodes.size;
        for (int i = 0; i < length; i++) {
            list.add(nodes.get(i).element);
        }

        if (region != REGION_SELF) {
            regions[region].getElements(list, r);
        } else {
            getAllElements(list, true);
        }
        return list;
    }

    public Array<T> getAllElements(Array<T> list, boolean firstCall) {
        if (regions != null) {
            regions[REGION_NW].getAllElements(list, false);
            regions[REGION_NE].getAllElements(list, false);
            regions[REGION_SW].getAllElements(list, false);
            regions[REGION_SE].getAllElements(list, false);
        }

        if (!firstCall) {
            int length = nodes.size;
            for (int i = 0; i < length; i++) {
                list.add(nodes.get(i).element);
            }
        }

        return list;
    }

    public void getAllTrees(Array<QuadTree<T>> list) {
        list.add(this);
        if (regions != null) {
            regions[REGION_NW].getAllTrees(list);
            regions[REGION_NE].getAllTrees(list);
            regions[REGION_SW].getAllTrees(list);
            regions[REGION_SE].getAllTrees(list);
        }
    }

    public T findNearestElementOverlap(QuadRectangle itemBound) {
        return findNearestElementOverlap(this,
                itemBound,
                itemBound.getCenterPoint().calculateDistance(new FloatPoint(itemBound.x, itemBound.y)));
    }

    private T findNearestElementOverlap(QuadTree<T> region, QuadRectangle bound, float nearestDistance) {
        T result = null;
        T temp;
        if (region.zone.overlaps(bound)) {
            if (region.regions != null) {
                temp = findNearestElementOverlap(region.regions[REGION_NW], bound, nearestDistance);
                if (temp != null) {
                    result = temp;
                }
                temp = findNearestElementOverlap(region.regions[REGION_NE], bound, nearestDistance);
                if (temp != null) {
                    result = temp;
                }
                temp = findNearestElementOverlap(region.regions[REGION_SW], bound, nearestDistance);
                if (temp != null) {
                    result = temp;
                }
                temp = findNearestElementOverlap(region.regions[REGION_SE], bound, nearestDistance);
                if (temp != null) {
                    result = temp;
                }
            } else {
                QuadNode<T> node;
                float distance;
                FloatPoint centerPoint = bound.getCenterPoint();
                for (int i = region.nodes.size - 1; i >= 0; i--) {
                    node = region.getQuadNode(i);
                    if (node.r.overlaps(bound) &&
                            (distance = centerPoint.calculateDistance(node.r.getCenterPoint())) < nearestDistance) {
                        nearestDistance = distance;
                        result = node.element;
                    }
                }
            }
        }
        return result;
    }


    public void findAllElementOverlap(Array<T> storeList, QuadRectangle bound) {
        findAllElementOverlap(this, storeList, bound);
    }

    private void findAllElementOverlap(QuadTree<T> region, Array<T> storeList, QuadRectangle bound) {
        if (region.zone.overlaps(bound)) {
            if (region.regions != null) {
                findAllElementOverlap(region.regions[REGION_NW], storeList, bound);
                findAllElementOverlap(region.regions[REGION_NE], storeList, bound);
                findAllElementOverlap(region.regions[REGION_SW], storeList, bound);
                findAllElementOverlap(region.regions[REGION_SE], storeList, bound);
            }

            QuadNode<T> temp;
            for (int i = region.nodes.size - 1; i >= 0; i--) {
                temp = region.getQuadNode(i);
                if (temp.r.overlaps(bound) && !storeList.contains(temp.element, true)) {
                    storeList.add(temp.element);
                }
            }

        }
    }

    public void findAllElementOverlapThenRemove(Array<T> storeList, QuadRectangle bound) {
        findAllElementOverlapThenRemove(this, storeList, bound);
    }

    private void findAllElementOverlapThenRemove(QuadTree<T> region, Array<T> storeList, QuadRectangle bound) {
        if (region.zone.overlaps(bound)) {
            if (region.regions != null) {
                findAllElementOverlapThenRemove(region.regions[REGION_NW], storeList, bound);
            }
            if (region.regions != null) {
                findAllElementOverlapThenRemove(region.regions[REGION_NE], storeList, bound);
            }
            if (region.regions != null) {
                findAllElementOverlapThenRemove(region.regions[REGION_SW], storeList, bound);
            }
            if (region.regions != null) {
                findAllElementOverlapThenRemove(region.regions[REGION_SE], storeList, bound);
            }

            QuadNode<T> temp;
            for (int i = region.nodes.size - 1; i >= 0; i--) {
                temp = region.getQuadNode(i);
                if (temp.r.overlaps(bound) && !storeList.contains(temp.element, true)) {
                    storeList.add(region.removeQuadNode(i).element);
                }
            }

            removeEmptyRegions(region);
        }
    }

    public void findThenRemoveQuadNode(QuadRectangle r, T element) {
        findThenRemoveQuadNode(this, r, element);
    }

    private void findThenRemoveQuadNode(QuadTree<T> parent, QuadRectangle r, T element) {
        int region = parent.findRegion(r, false);
        if (region == REGION_SELF) {
            Array<QuadNode<T>> nodes = parent.nodes;
            for (int i = nodes.size - 1; i >= 0; i--) {
                if (nodes.get(i).element == element) {
                    parent.nodes.removeIndex(i);
                    break;
                }
            }
            removeEmptyRegions(parent);
        } else {
            parent.findThenRemoveQuadNode(parent.regions[region], r, element);
        }
    }

    private void removeEmptyRegions(QuadTree<T> quadTree) {
        if (quadTree != null && quadTree.nodes.size == 0) {
            if (quadTree.regions == null) {
                removeEmptyRegions(quadTree.parent);
            } else if (quadTree.regions[REGION_NW].isEmptyRegion() &&
                    quadTree.regions[REGION_NE].isEmptyRegion() &&
                    quadTree.regions[REGION_SE].isEmptyRegion() &&
                    quadTree.regions[REGION_SW].isEmptyRegion()) {
                quadTree.regions = null;
                removeEmptyRegions(quadTree.parent);
            }
        }
    }

    public boolean isEmptyRegion() {
        return nodes.size == 0 && regions == null;
    }
}
