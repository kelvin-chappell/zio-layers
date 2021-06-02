package ziolayers

import zio.Has

package object service {
  type Logging = Has[Logging.Service]
}
