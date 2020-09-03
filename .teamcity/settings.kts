import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.Swabra
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.swabra
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2018.2"

project {
    vcsRoot(PetclinicVcs)
    buildType(Build)

    features {
        feature {
            type = "storage_settings"
            id = "PROJECT_EXT_368"
            param("storage.name", "storage from dsl (229) ((new name))")
            param("storage.type", "S3_storage")
        }

    }
}

object Build : BuildType({
    name = "Build"
    artifactRules = "target/*jar"

    vcs {
        root(PetclinicVcs)
    }
    steps {
        maven {
            goals = "clean package"
        }
    }
    triggers {
        vcs {
            groupCheckinsByCommitter = true
        }
    }
})

object PetclinicVcs : GitVcsRoot({
    name = "PetclinicVcs"
    url = "https://github.com/spring-projects/spring-petclinic.git"
})


fun wrapWithFeature(buildType: BuildType, featureBlock: BuildFeatures.() -> Unit): BuildType {
    buildType.features {
        featureBlock()
    }
    return buildType
}