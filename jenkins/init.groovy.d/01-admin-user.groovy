// Creates the Jenkins admin user on first start.
// The password comes from the JENKINS_ADMIN_PASSWORD environment variable (set in .env).
import jenkins.model.Jenkins
import hudson.security.HudsonPrivateSecurityRealm
import hudson.security.FullControlOnceLoggedInAuthorizationStrategy

def jenkins = Jenkins.get()
def user = System.getenv('JENKINS_ADMIN_USER') ?: 'admin'
def password = System.getenv('JENKINS_ADMIN_PASSWORD')

if (!(jenkins.securityRealm instanceof HudsonPrivateSecurityRealm) && password) {
    def realm = new HudsonPrivateSecurityRealm(false)
    realm.createAccount(user, password)
    jenkins.securityRealm = realm

    def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
    strategy.allowAnonymousRead = false
    jenkins.authorizationStrategy = strategy
    jenkins.save()
    println "Admin user '${user}' created"
}
