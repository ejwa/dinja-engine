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
package com.ejwa.dinja.demo.activity;

import android.os.Bundle;
import com.ejwa.dinja.engine.activity.DinjaActivity;
import com.ejwa.dinja.engine.controller.animator.DelayAnimator;
import com.ejwa.dinja.engine.controller.animator.RotationAnimator;
import com.ejwa.dinja.engine.controller.animator.ScaleAnimator;
import com.ejwa.dinja.engine.controller.animator.script.ArrayAnimatorScript;
import com.ejwa.dinja.engine.controller.animator.script.LoopAnimatorScript;
import com.ejwa.dinja.engine.controller.animator.script.PingPongAnimatorScript;
import com.ejwa.dinja.engine.model.Camera;
import com.ejwa.dinja.engine.model.Scene;
import com.ejwa.dinja.engine.model.ease.CubicInOutEase;
import com.ejwa.dinja.engine.model.mesh.geometry.Plane;
import com.ejwa.dinja.engine.util.TextureLoader;
import com.ejwa.dinja.engine.view.DebugView;
import com.ejwa.dinja.engine.view.SceneView;
import org.openmali.vecmath2.Quaternion4f;
import org.openmali.vecmath2.Vector3f;

public class DancingLettersActivity extends DinjaActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Quaternion4f letterRotation = new Quaternion4f(0, 0, -0.15f, 1);
		final DancingLetter letterD = new DancingLetter("D", "dinja_txt_d.png", -1, 0.25f, 0.5f, 0.5f, letterRotation);
		final DancingLetter letterI = new DancingLetter("I", "dinja_txt_i.png", -0.5f, 0.25f, 0.5f, 0.5f, letterRotation);
		final DancingLetter letterN = new DancingLetter("N", "dinja_txt_n.png", 0, 0.25f, 0.5f, 0.5f, letterRotation);
		final DancingLetter letterJ = new DancingLetter("J", "dinja_txt_j.png", 0.5f, 0.25f, 0.5f, 0.5f, letterRotation);
		final DancingLetter letterA = new DancingLetter("A", "dinja_txt_a.png", 1, 0.25f, 0.5f, 0.5f, letterRotation);
		final DancingLetter lettersEngine = new DancingLetter("ENGINE", "dinja_txt_engine.png", 0, -0.25f, 2.4f, 0.3f);
		final Scene scene = new Scene(new Camera(), letterD, letterI, letterN, letterJ, letterA, lettersEngine);

		registerView(new SceneView(scene));
		registerView(new DebugView(0.25f, this, scene));

		registerController(new UpperLetterAnimator(letterD, new Quaternion4f(0, 0, 0.15f, 1), 1.5f));
		registerController(new UpperLetterAnimator(letterI, new Quaternion4f(0, 0, 0.15f, 1), 1.525f));
		registerController(new UpperLetterAnimator(letterN, new Quaternion4f(0, 0, 0.15f, 1), 1.55f));
		registerController(new UpperLetterAnimator(letterJ, new Quaternion4f(0, 0, 0.15f, 1), 1.575f));
		registerController(new UpperLetterAnimator(letterA, new Quaternion4f(0, 0, 0.15f, 1), 1.6f));
		registerController(new LowerLetterAnimator(lettersEngine, 1.1f, 1.25f, 1));
	}

	private class UpperLetterAnimator extends LoopAnimatorScript {
		public UpperLetterAnimator(DancingLetter letter, Quaternion4f rotation, float duration) {
			super(new PingPongAnimatorScript(new RotationAnimator(letter, rotation, duration, CubicInOutEase.class)));
		}
	}

	private class LowerLetterAnimator extends LoopAnimatorScript {
		public LowerLetterAnimator(DancingLetter letter, float scale, float duration, float pause) {
			super(new PingPongAnimatorScript(new ArrayAnimatorScript(
			      new ScaleAnimator(letter, scale, duration, CubicInOutEase.class), new DelayAnimator(pause))));
		}
	}

	private class DancingLetter extends Plane {
		public DancingLetter(String name, String textureName, float x, float y, float width, float height) {
			super(name, width, height, 1);
			setTexture(TextureLoader.load(getAssets(), textureName));
			getTranslator().set(new Vector3f(x, y, 0));
		}

		public DancingLetter(String name, String textureName, float x, float y, float width, float height, Quaternion4f rotation) {
			this(name, textureName, x, y, width, height);
			getRotator().set(rotation);
		}
	}
}
