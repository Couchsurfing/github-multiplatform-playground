import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {

    }
    
    var component = AppComponent.companion.create(key: "")
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
