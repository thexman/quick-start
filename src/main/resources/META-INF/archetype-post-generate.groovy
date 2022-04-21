dir = new File(new File(request.outputDirectory), request.artifactId)
gitignoreTemplate = new File(dir, ".gitignore-template")
gitignore = new File(dir, ".gitignore")
gitignoreTemplate.renameTo(gitignore)
