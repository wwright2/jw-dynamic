package dynacode;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * A wrapper to ease the use of jdk.compiler.Main.
 * 
 * @author liyang
 */
public final class Javac {

	String classpath;
	String outputdir;
	String sourcepath;
	String bootclasspath;
	String extdirs;
	String encoding;
	String target;

	public Javac(String classpath, String outputdir) {
		this.classpath = classpath;
		this.outputdir = outputdir;
	}

	/**
	 * Compile the given source files.
	 * 
	 * @param srcFiles
	 * @return null if success; or compilation errors
	 */
	public String compile(String srcFiles[]) {
		StringWriter err = new StringWriter();
		PrintWriter errPrinter = new PrintWriter(err);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		
		Iterable<String> args = buildJavacArgs();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

//        // This sets up the class path that the compiler will use.
//        // I've added the .jar file that contains the DoStuff interface within in it...
//        List<String> optionList = new ArrayList<String>();
//        optionList.add("-classpath");
//        optionList.add(System.getProperty("java.class.path") + ";dist/InlineCompiler.jar");

//        Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(helloWorldJava));
        Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjects(srcFiles);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, 
        							args, null, compilationUnit);
        
//		int resultCode = compiler(args, errPrinter);
        int resultCode = 0;

        if (task.call()) {
            System.out.println("Yipe");
        } else {
        	resultCode = 1;
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                System.out.format("Error on line %d in %s%n",
                        diagnostic.getLineNumber(),
                        diagnostic.getSource().toUri());
            }
        }
		errPrinter.close();
		return (resultCode == 0) ? null : err.toString();
	}

	public String compile(File srcFiles[]) {
		String paths[] = new String[srcFiles.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = srcFiles[i].getAbsolutePath();
		}
		return compile(paths);
	}


	private void buildArgList (ArrayList <String> args){
		if (classpath != null) {
			args.add("-classpath");
			args.add(classpath);
		}
		if (outputdir != null) {
			args.add("-d");
			args.add(outputdir);
		}
		if (sourcepath != null) {
			args.add("-sourcepath");
			args.add(sourcepath);
		}
		if (bootclasspath != null) {
			args.add("-bootclasspath");
			args.add(bootclasspath);
		}
		if (extdirs != null) {
			args.add("-extdirs");
			args.add(extdirs);
		}
		if (encoding != null) {
			args.add("-encoding");
			args.add(encoding);
		}
		if (target != null) {
			args.add("-target");
			args.add(target);
		}
	}

	private Iterable<String> buildJavacArgs() {
		ArrayList <String>args = new ArrayList<String>();
		buildArgList(args);
		return args;
	}
	
	private Iterable<String> buildJavacArgs(String srcFiles[]) {
		ArrayList <String>args = new ArrayList<String>();
		buildArgList(args);

		for (int i = 0; i < srcFiles.length; i++) {
			args.add(srcFiles[i]);
		}
//		return (String[]) args.toArray(new String[args.size()]);
		return args;
	}

	public String getBootclasspath() {
		return bootclasspath;
	}

	public void setBootclasspath(String bootclasspath) {
		this.bootclasspath = bootclasspath;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getExtdirs() {
		return extdirs;
	}

	public void setExtdirs(String extdirs) {
		this.extdirs = extdirs;
	}

	public String getOutputdir() {
		return outputdir;
	}

	public void setOutputdir(String outputdir) {
		this.outputdir = outputdir;
	}

	public String getSourcepath() {
		return sourcepath;
	}

	public void setSourcepath(String sourcepath) {
		this.sourcepath = sourcepath;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
