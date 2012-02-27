/*
 * Copyright © 2011-2012 Ejwa Software. All rights reserved.
 *
 * This file is part of Dinja Engine. Dinja Engine is a OpenGLES2
 * 3D engine with physics support developed for the Android platform.
 *
 * Dinja Engine is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Dinja Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with Dinja Engine. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.ejwa.dinja.engine.model.node;

import com.ejwa.dinja.engine.controller.SceneController;
import java.util.List;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Point3f;

/**
 * An interface that describes an node in the scene graph. Any member of the scene graph needs to implement this interface.
 * A node can hold a children of nodes and also holds a model matrix (describing local transformations) and a world matrix
 * (describing global transformations).
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 */
public interface INode {
	/**
	 * Returns the model matrix which describes the tranformation (rotation, scale and translation) locally to
	 * this node.
	 *
	 * @return The model matrix of this node.
	 */
	Matrix4f getModelMatrix();

	/**
	 * Propagates the model matrix of this node to every child node of this node, in effect calling
	 * {@link Matrix4f#mul(Matrix4f, Matrix4f)} on each child node. The {@link #propagateModelMatrixDeep()} method is
	 * a recursive version of this method.
	 *
	 * @see #propagateModelMatrixDeep()
	 */
	void propagateModelMatrix();

	/**
	 * Recursively propagates the model matrix of this node to every child node of this node.
	 *
	 * @see #propagateModelMatrix()
	 */
	void propagateModelMatrixDeep();

	/**
	 * Returns the world matrix which describes the tranformation (rotation, scale and translation) absolute
	 * to the world. In effect, this means that this matrix holds the total of all transformation from the root node
	 * (normaly the scene) all the way up to this node.
	 *
	 * @return The world matrix of this node. This matrix is <b>read only</b>. If you modify it by this reference it will
	 * just be overwritten the next frame by the {@link SceneController}.
	 */
	Matrix4f getWorldMatrix();

	/**
	 * Returns the name of this node. Each node that is attached to the hierarchy of the root node (usually the scene) needs
	 * to define a unique name. The name can later be used as a means of fetching a node from the hierarchy using
	 * {@link IRootNode#getNodeDeep(java.lang.String)}. The uniqueness of the name is checked whenever this node is being
	 * attached to a hierarchy of a root node either directly or via one of it's parent nodes.
	 *
	 * @return The unique name of this node.
	 */
	String getName();

	/**
	 * Returns the root node that this node is attached to.
	 *
	 * @return The root node of this node or <code>null</code> if this node isn't attached to a root node.
	 */
	IRootNode getRoot();

	/**
	 * Sets the root node that this node will be part of. This is called by Dinja Engine whenever a node is being
	 * attached to a root node. This method is also called whenever a node is being detached from a root node.
	 * Normally, you should never have to call this method manually.
	 *
	 * @param root The root node that this node should belong to.
	 */
	void setRoot(IRootNode root);

	/**
	 * Adds the specified nodes to the list of child nodes that this node holds. If this node is attached to a root node,
	 * the newly added nodes will recursively inherit the same root node.
	 *
	 * @param nodes The nodes to add to this node.
	 */
	void addNodes(INode ...nodes);

	/**
	 * Removes the specified nodes from the list of child nodes that this node holds. If this node is attached to a
	 * root node, the nodes being removed will have their root node reset to <code>null</code>.
	 *
	 * @param nodes The nodes to remove from this node.
	 */
	void removeNodes(INode ...nodes);
	void removeNodes(String ...nodeNames);
	List<INode> getNodes();
	INode getNode(String nodeName);
	INode getNodeClosestToPointDeep(Point3f point);
}
