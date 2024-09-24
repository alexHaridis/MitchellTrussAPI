<h1> Exploring Mitchell Structures </h1>

<p><img src="https://github.com/alexHaridis/MitchellTrussAPI/assets/9630033/d31a31bf-4345-451f-b429-25046c6fce3f" alt="Mitchell Structure API" width="380" align="Right"> 
This is a standalone application for exploring optimum Mitchell trusses written in Java. It uses the <a href="https://www.processing.org">Processing API</a> for graphic elements, rendering, and GUI objects. This implementation builds on a <em>geometric solution</em> of discrete Mitchell trusses in their basic symmetric form: a discrete optimum truss supported by two fixed points (positioned along the same vertical line) and a vertical point load positioned in between the supports at a fixed distance.</p>

<p>The geometric solution is described in detail in A. Mazurek, W.F. Baker, C. Tort (2011) <a href="http://link.springer.com/article/10.1007/s00158-010-0559-x">"Geometrical aspects of optimum truss like structures"</a>, <em>Structural and Multidisciplinary Optimization</em>, 43 (2). Custom computational abstractions were developed along with appropriate algorithms to provide a parametric framework for generating the geometry of optimum trusses. More specifically, according to the selected parameters and values 21 trusses in total can be calculated. The parameters considered are: <em>h</em>, the vertical distance between the supports, <em>L</em> the horizontal distance between the supports and the point load applied at the tip of the structure, and <em>n</em> the total number of bars in the system. The forces on each member are computed with a routine that implements the <a href="https://en.wikibooks.org/wiki/Statics/Method_of_Joints">method of joints</a>.<br><br>
For more information on this geometric solution and its computational implementation, please read the uploaded PDF writeup called <em>MitchellStructureWriteup.pdf</em>.</p>

<p>Dependencies: <a href="http://math.nist.gov/javanumerics/jama/">JAMA matrix package</a>, <a href="http://www.sojamo.de/libraries/controlP5/">ControlP5</a>, <a href="http://toxiclibs.org">toxiclibs</a>.
</p>

<h3> Acknowledgements </h3>
<p>This application was developed in the Spring semester of 2015 by Alexandros Haridis, in the <a href="http://digitalstructures.mit.edu/page/design" target="_blank">Digital Structures Group</a> led by Prof. Caitlin Mueller at the <a href="https://architecture.mit.edu/" target="_blank">MIT Department of Architecture</a>.</p>



